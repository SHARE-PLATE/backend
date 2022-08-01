package louie.hanse.shareplate.repository;

import louie.hanse.shareplate.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
