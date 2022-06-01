package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
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

	@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");
		memberJpaRepository.save(member1);
		memberJpaRepository.save(member2);

		//단건 조회 검증
		Member findMember1 =
				memberJpaRepository.findById(member1.getId()).get();
		Member findMember2 =
				memberJpaRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);

		//리스트 조회 검증
		List<Member> all = memberJpaRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		//카운트 검증
		long count = memberJpaRepository.count();
		assertThat(count).isEqualTo(2);

		//삭제 검증
		memberJpaRepository.delete(member1);
		memberJpaRepository.delete(member2);
		long deletedCount = memberJpaRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}

	@Test
	void findByUsernameAndAgeGreaterThen() {
		// given
		Member member1 = new Member("member1", 10);
		Member member2 = new Member("member2", 20);
		memberJpaRepository.save(member1);
		memberJpaRepository.save(member2);

		// when
		List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("member2", 15);
		// then
		assertThat(result.get(0).getUsername()).isEqualTo("member2");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	void testNamedQuery() {
		// given
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberJpaRepository.save(member1);
		memberJpaRepository.save(member2);

		// when
		List<Member> result = memberJpaRepository.findByUsername("AAA");

		// then
		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(10);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	void paging() {
		// given
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 10));
		memberJpaRepository.save(new Member("member3", 10));
		memberJpaRepository.save(new Member("member4", 10));
		memberJpaRepository.save(new Member("member5", 10));

		int age = 10;
		int offset = 0;
		int limit = 3;

		// when
		List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
		long totalCount = memberJpaRepository.totalCount(age);

		// then
		assertThat(members.size()).isEqualTo(3);
		assertThat(totalCount).isEqualTo(5);
	}

	@Test
	public void bulkUpdate() throws Exception {
		//given
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 19));
		memberJpaRepository.save(new Member("member3", 20));
		memberJpaRepository.save(new Member("member4", 21));
		memberJpaRepository.save(new Member("member5", 40));
		//when
		int resultCount = memberJpaRepository.bulkAgePlus(20);
		//then
		assertThat(resultCount).isEqualTo(3);
	}
}
