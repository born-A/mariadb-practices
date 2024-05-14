-- 테이블간 조인(JOIN) SQL 문제입니다.

-- 문제 1. 
-- 현재 급여가 많은 직원부터 직원의 사번, 이름, 그리고 연봉을 출력 하시오.
select a.emp_no '사번', a.first_name '이름', b.salary '연봉'
from employees a, salaries b
where a.emp_no = b.emp_no
	and b.to_date = '9999-01-01'
order by b.salary desc;

-- 문제2.
-- 전체 사원의 사번, 이름, 현재 직책을 이름 순서로 출력하세요.
select a.emp_no '사번', concat(a.first_name,' ',a.last_name) '이름', b.title '직책'
from employees a, titles b
where a.emp_no = b.emp_no
and b.to_date = '9999-01-01'
order by a.first_name;

-- 문제3.
-- 전체 사원의 사번, 이름, 현재 부서를 이름 순서로 출력하세요.
select a.emp_no '사번', concat(a.first_name,' ',a.last_name) '이름', b.dept_name '부서'
from employees a, departments b, dept_emp c
where a.emp_no = c.emp_no
and b.dept_no = c. dept_no
and c.to_date = '9999-01-01'
order by a.first_name;

-- 문제4.
-- 전체 사원의 사번, 이름, 연봉, 직책, 부서를 모두 이름 순서로 출력합니다.
select a.emp_no '사번', concat(a.first_name,' ',a.last_name) '이름', d.salary '연봉', b.dept_name '직책', e.title '부서'
from employees a, departments b, dept_emp c, salaries d, titles e
where a.emp_no = c.emp_no
and b.dept_no = c. dept_no
and d.emp_no = a.emp_no
and e.emp_no = a.emp_no
and c.to_date = '9999-01-01'
and d.to_date = '9999-01-01'
and e.to_date = '9999-01-01'
order by a.first_name;

-- 문제5.
-- ‘Technique Leader’의 직책으로 과거에 근무한 적이 있는 모든 사원의 사번과 이름을 출력하세요. (현재 ‘Technique Leader’의 직책(으로 근무하는 사원은 고려하지 않습니다.) 이름은 first_name과 last_name을 합쳐 출력 합니다.
select a.emp_no '사번' ,  concat(a.first_name,' ',a.last_name) '이름'
from employees a, titles b
where a.emp_no = b.emp_no
and b.to_date < now()
and b.from_date < now()
and b.title =  "Technique Leader";

-- 문제6.
-- 직원 이름(last_name) 중에서 S(대문자)로 시작하는 직원들의 이름, 부서명, 직책을 조회하세요.
select concat(a.first_name, ' ', a.last_name) '이름', b.dept_name '부서명', c.title '직책'
from employees a, departments b, titles c, dept_emp d
where a.emp_no = c.emp_no
and a.emp_no = d.emp_no
and b.dept_no = d.dept_no
and a.last_name LIKE 'S%';

-- 문제7.
-- 현재, 직책이 Engineer인 사원 중에서 현재 급여가 40000 이상인 사원을 급여가 큰 순서대로 출력하세요.
-- projection : 사번 이름(first_name) 급여 타이틀
select a.emp_no '사번', a.first_name '이름', b.salary '급여', c.title '직책'
from employees a, salaries b, titles c
where a.emp_no = b.emp_no
and a.emp_no = c.emp_no
and b.to_date = '9999-01-01'
and c.to_date = '9999-01-01'
and b.salary >= 40000
and c.title = 'Engineer'
order by b.salary desc;

-- 문제8.
-- 현재 급여가 50000이 넘는 직책을 직책, 급여로 급여가 큰 순서대로 출력하시오
select a.title as '직책', b.salary as '급여'
from titles a, salaries b
where a.emp_no = b.emp_no
and a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
and b.salary >= 50000
order by b.salary desc;

-- 문제9.
-- 현재, 부서별 평균 연봉을 연봉이 큰 부서 순서대로 출력하세요.
-- projection: 부서명 평균연봉
select  d.dept_name '부서명', avg(b.salary) '평균연봉'
from titles a, salaries b, dept_emp c, departments d
where a.emp_no = b.emp_no
and a.emp_no = c.emp_no
and c.dept_no = d.dept_no
and a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
and c.to_date = '9999-01-01'
group by d.dept_name
order by avg(b.salary) desc;

-- 문제10.
-- 현재, 직책별 평균 연봉을 연봉이 큰 직책 순서대로 출력하세요.
-- projection: 직책명 평균연봉
select a.title '직책명', avg(b.salary) '평균연봉'
from titles a, salaries b
where a.emp_no = b.emp_no
and a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
group by a.title
order by avg(b.salary) desc;