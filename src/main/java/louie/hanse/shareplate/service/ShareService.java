package louie.hanse.shareplate.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import louie.hanse.shareplate.domain.Member;
import louie.hanse.shareplate.domain.Share;
import louie.hanse.shareplate.repository.ShareRepository;
import louie.hanse.shareplate.type.MineType;
import louie.hanse.shareplate.web.dto.share.ShareMineSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareRegisterRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchRequest;
import louie.hanse.shareplate.web.dto.share.ShareSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final AmazonS3 amazonS3Client;

    @Transactional
    public void register(ShareRegisterRequest request, Long memberId) throws IOException {
//        TODO : 커스텀 Exception 처리
        Member member = memberService.findMember(memberId);
        Share share = request.toEntity(member);
        for (MultipartFile image : request.getImages()) {
            String uploadedImageUrl = uploadImage(image);
            share.addShareImage(uploadedImageUrl);
        }
        shareRepository.save(share);
    }

    public List<ShareSearchResponse> searchAroundMember(
        ShareSearchRequest shareSearchRequest, Long memberId) {
        Member member = memberService.findMember(memberId);
        List<Share> shares = shareRepository.searchAroundMember(member, shareSearchRequest);
        return shares.stream()
            .map(ShareSearchResponse::new)
            .collect(Collectors.toList());
    }

    public List<ShareSearchResponse> searchMine(
        ShareMineSearchRequest shareMineSearchRequest, Long memberId) {
        MineType mineType = shareMineSearchRequest.getMineType();
        List<Share> shares = null;
        if (mineType.isEntry()) {
            shares = shareRepository.findWithEntry(memberId);
        }
        if (mineType.isWriter()) {
            shares = shareRepository.findByWriterId(memberId);
        }
        if (mineType.isWish()) {
            shares = shareRepository.findWithWish(memberId);
        }
        return shares.stream()
            .map(ShareSearchResponse::new)
            .collect(Collectors.toList());
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
