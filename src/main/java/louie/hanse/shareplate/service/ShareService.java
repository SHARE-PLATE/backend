package louie.hanse.shareplate.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.ChatRoom;
import louie.hanse.shareplate.domain.Entry;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.exception.GlobalException;
import louie.hanse.shareplate.exception.type.ShareExceptionType;
import louie.hanse.shareplate.jwt.JwtProvider;
import louie.hanse.shareplate.repository.EntryRepository;
import louie.hanse.shareplate.repository.ShareRepository;
import louie.hanse.shareplate.repository.WishRepository;
import louie.hanse.shareplate.type.ChatRoomType;
import louie.hanse.shareplate.type.MineType;
import louie.hanse.shareplate.type.ShareType;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationResponse;
import louie.hanse.shareplate.web.dto.share.ShareDetailResponse;
import louie.hanse.shareplate.web.dto.share.ShareEditRequest;
import louie.hanse.shareplate.web.dto.share.ShareMineSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareRecommendationRequest;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchResponse;
import louie.hanse.shareplate.web.dto.share.ShareWriterResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ShareService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${file.upload.location}")
    private String fileUploadLocation;

    private final MemberService memberService;
    private final ShareRepository shareRepository;
    private final WishRepository wishRepository;
    private final EntryRepository entryRepository;
    private final AmazonS3 amazonS3Client;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long register(ShareRegisterRequest request, Long memberId) throws IOException {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = request.toEntity(member);
        for (MultipartFile image : request.getImages()) {
            String uploadedImageUrl = uploadImage(image);
            share.addShareImage(uploadedImageUrl);
        }

        if (!ObjectUtils.isEmpty(request.getHashtags())) {
            for (String contents : request.getHashtags()) {
                share.addHashtag(contents);
            }
        }

        Entry entry = new Entry(share, member);
        entryRepository.save(entry);
        new ChatRoom(member, share, ChatRoomType.ENTRY);
        shareRepository.save(share);
        return share.getId();
    }

    public List<ShareSearchResponse> searchAroundMember(
        ShareSearchRequest shareSearchRequest) {
        List<Share> shares = shareRepository.searchAroundMember(shareSearchRequest);
        return shares.stream()
            .map(ShareSearchResponse::new)
            .collect(Collectors.toList());
    }

    public List<ShareSearchResponse> searchMine(
        ShareMineSearchRequest shareMineSearchRequest, Long memberId) {
        ShareType type = shareMineSearchRequest.getShareType();
        boolean expired = shareMineSearchRequest.isExpired();
        LocalDateTime currentDateTime = LocalDateTime.now();

        Map<MineType, Supplier<List<Share>>> shareFindMapByMineType = Map.of(
            MineType.ENTRY, () -> shareRepository.findWithEntryByMemberIdAndTypeAndNotWriteByMeAndIsExpired(
                memberId, type, expired, currentDateTime),
            MineType.WRITER, () -> shareRepository.findByWriterIdAndTypeAndIsExpired(
                memberId, type, expired, currentDateTime),
            MineType.WISH, () -> shareRepository.findWithWishByMemberIdAndTypeAndIsExpired(
                memberId, type, expired, currentDateTime)
        );

        List<Share> shares = shareFindMapByMineType.get(shareMineSearchRequest.getMineType()).get();
        return shares.stream()
            .map(ShareSearchResponse::new)
            .collect(Collectors.toList());
    }

    public ShareDetailResponse getDetail(Long id, String accessToken) {
        Share share = findByIdOrElseThrow(id);
        share.isCanceledThrowException();

        boolean check = true;
        Long memberId = null;
        try {
            if (StringUtils.hasText(accessToken)) {
                jwtProvider.verifyAccessToken(accessToken);
                memberId = jwtProvider.decodeMemberId(accessToken);
            } else {
                check = false;
            }
        } catch (JWTVerificationException e) {
            check = false;
        }

        boolean wish = false;
        boolean entry = false;
        if (check) {
            wish = wishRepository.existsByMemberIdAndShareId(memberId, id);
            entry = entryRepository.existsByMemberIdAndShareId(memberId, id);
        }

        if (check && !entry) {
            entry = shareRepository.existsByIdAndWriterId(id, memberId);
        }
        return new ShareDetailResponse(share, wish, entry);
    }

    @Transactional
    public void edit(ShareEditRequest request, Long id, Long memberId) throws IOException {
        Share findShare = findWithWriterByIdOrElseThrow(id);
        Member member = memberService.findByIdOrElseThrow(memberId);
        findShare.isNotWriterThrowException(member);
        findShare.isClosedThrowException();
        findShare.isCanceledThrowException();

        Share share = request.toEntity(id, member);
        for (MultipartFile image : request.getImages()) {
            String uploadImageUrl = uploadImage(image);
            share.addShareImage(uploadImageUrl);
        }

        if (!ObjectUtils.isEmpty(request.getHashtags())) {
            for (String contents : request.getHashtags()) {
                share.addHashtag(contents);
            }
        }
        shareRepository.save(share);
    }

    @Transactional
    public void cancel(Long id, Long memberId) {
        Member member = memberService.findByIdOrElseThrow(memberId);
        Share share = findWithWriterByIdOrElseThrow(id);
        share.isNotWriterThrowException(member);
        share.cancel();
    }

    public Share findByIdOrElseThrow(Long id) {
        return shareRepository.findById(id)
            .orElseThrow(() -> new GlobalException(ShareExceptionType.SHARE_NOT_FOUND));
    }

    public List<ShareRecommendationResponse> recommendationAroundMember(ShareRecommendationRequest request) {
        List<ShareRecommendationResponse> shareRecommendationResponses = shareRepository
            .recommendationAroundMember(request);
        Collections.shuffle(shareRecommendationResponses);
        return shareRecommendationResponses;
    }

    public ShareWriterResponse getWriteByMember(Long writerId) {
        Member writer = memberService.findByIdOrElseThrow(writerId);
        return new ShareWriterResponse(writer);
    }

    private Share findWithWriterByIdOrElseThrow(Long id) {
        return shareRepository.findWithWriterById(id)
            .orElseThrow(() -> new GlobalException(ShareExceptionType.SHARE_NOT_FOUND));
    }

    private String uploadImage(MultipartFile image) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        String originalImageName = image.getOriginalFilename();
        String ext = originalImageName.substring(originalImageName.lastIndexOf(".") + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = fileUploadLocation + "/" + storeFileName;

        try (InputStream inputStream = image.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }

}
