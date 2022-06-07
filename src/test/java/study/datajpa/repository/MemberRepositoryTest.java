package study.datajpa.repository;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private EntityManager em;

	@Test
	void test() {
		// given
		Member member = new Member("memberA");
		Member savedMember = memberRepository.save(member);

		// when
		Member findMember = memberRepository.findById(savedMember.getId()).get();

		// then
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
		assertThat(findMember).isEqualTo(savedMember);
	}

	@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");
		memberRepository.save(member1);
		memberRepository.save(member2);

		//단건 조회 검증
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);

		//리스트 조회 검증
		List<Member> all = memberRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		//카운트 검증
		long count = memberRepository.count();
		assertThat(count).isEqualTo(2);

		//삭제 검증
		memberRepository.delete(member1);
		memberRepository.delete(member2);
		long deletedCount = memberRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}

	@Test
	void findByUsernameAndAgeGreaterThen() {
		// given
		Member member1 = new Member("member1", 10);
		Member member2 = new Member("member2", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);

		// when
		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("member2", 15);
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
		memberRepository.save(member1);
		memberRepository.save(member2);

		// when
		List<Member> result = memberRepository.findByUsername("AAA");

		// then
		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(10);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	void testNamedQuery2() {
		// given
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);

		// when
		List<Member> result = memberRepository.findUser("AAA", 10);

		// then
		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(10);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	void testFindUsernameList() {
		// given
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);

		// when
		List<String> usernameList = memberRepository.findUsernameList();
		usernameList.forEach(System.out::println);

		// then
	}

	@Test
	void findMemberDto() {
		// given
		Team teamA = new Team("teamA");
		teamRepository.save(teamA);

		Member member1 = new Member("AAA", 10);
		member1.setTeam(teamA);
		memberRepository.save(member1);

		// when
		List<MemberDto> memberDto = memberRepository.findMemberDto();
		memberDto.forEach(System.out::println);

		// then
	}

	@Test
	void testFindByNames() {
		// given
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);

		// when
		List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
		result.forEach(System.out::println);

		// then
	}

	@Test
	void returnType() {
		// given
		Member member1 = new Member("AAA", 10);
		Member member2 = new Member("BBB", 20);
		memberRepository.save(member1);
		memberRepository.save(member2);

		// when
		List<Member> result = memberRepository.findListByUsername("AAA");
		result.forEach(System.out::println);

		// then
	}

	@Test
	void paging() {
		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
		int age = 10;

		// when
		Page<Member> page = memberRepository.findByAge(age, pageRequest);

		// then
		List<Member> content = page.getContent();
		long totalElements = page.getTotalElements();
		//		content.forEach(System.out::println);
		//		System.out.println("totalElements = " + totalElements);

		assertThat(content.size()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.isFirst()).isTrue();
		assertThat(page.hasNext()).isTrue();

		Slice<Member> slice = memberRepository.findSliceByAge(age, pageRequest);

	}

	@Test
	void slice() {
		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Sort.Direction.DESC, "username"));
		int age = 10;

		// when
		Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

		// then
		List<Member> content = page.getContent();
		//		content.forEach(System.out::println);
		//		System.out.println("totalElements = " + totalElements);

		assertThat(content.size()).isEqualTo(2);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.isFirst()).isTrue();
		assertThat(page.hasNext()).isTrue();
	}

	@Test
	public void bulkUpdate() throws Exception {
		//given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 19));
		memberRepository.save(new Member("member3", 20));
		memberRepository.save(new Member("member4", 21));
		memberRepository.save(new Member("member5", 40));
		//when
		int resultCount = memberRepository.bulkAgePlus(20);
		//then
		assertThat(resultCount).isEqualTo(3);
	}

	@Test
	public void findMemberLazy() {
		// given
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);

		Member member1 = new Member("AAA", 10);
		member1.setTeam(teamA);
		Member member2 = new Member("BBB", 20);
		member2.setTeam(teamB);

		memberRepository.save(member1);
		memberRepository.save(member2);

		// when
		em.flush();
		em.clear();
		//when
//		List<Member> members = memberRepository.findAll();
		List<Member> members = memberRepository.findMemberFetchJoin();
		//then
		for (Member member : members) {
			member.getTeam().getName();
			//Hibernate 기능으로 확인
			Hibernate.isInitialized(member.getTeam());
			//JPA 표준 방법으로 확인
			PersistenceUnitUtil util = em.getEntityManagerFactory().getPersistenceUnitUtil();
			util.isLoaded(member.getTeam());
		}

		// then
	}

	@Test
	public void queryHint() throws Exception {
		//given
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);
		em.flush();
		em.clear();

//		Optional<Member> findMember = memberRepository.findById(member1.getId());
//		findMember.get().setUsername("memeber2");

		//when
		Member member = memberRepository.findReadOnlyByUsername("member1");
		member.setUsername("member2");
		em.flush(); //Update Query 실행X
	}

	@Test
	void findMemberCustom() {
		// given

		// when
		List<Member> memberCustom = memberRepository.findMemberCustom();

		// then
	}
}
