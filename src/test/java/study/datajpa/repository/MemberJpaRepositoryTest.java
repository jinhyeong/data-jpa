package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

	@Autowired
	private MemberJpaRepository memberJpaRepository;

	@Test
	void test() {
		// given
		Member member = new Member("memberA");
		Member saveMember = memberJpaRepository.save(member);

		// when
		Member findMember = memberJpaRepository.find(saveMember.getId());

		// then
		assertThat(findMember.getId()).isEqualTo(saveMember.getId());
		assertThat(findMember.getUsername()).isEqualTo(saveMember.getUsername());
		assertThat(findMember).isEqualTo(saveMember);
	}
}
