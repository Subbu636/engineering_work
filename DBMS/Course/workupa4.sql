-- explain select l.name, l.rollNo, l.tc from 
-- (select x.name, x.rollNo, sum(c.credits) as tc from (select s.name, s.rollNo from student s where not exists 
-- (select * from enrollment e where e.rollNo = s.rollNo and e.grade <> 'S' and e.grade <> 'A')) x, enrollment e, course c 
-- where x.rollNo = e.rollNo and e.courseId = c.courseId group by x.rollNo) l where l.tc >= 10
-- explain select s.name, s.rollNo from student s,(select y.empId from  professor y where y.startYear <= all (select x.startYear from professor x)) p where s.advisor = p.empId or exists ( select * from enrollment e where e.rollNo = s.rollNo and exists (select * from teaching t where t.sem = e.sem and t.year = e.year and t.courseId = e.courseId and t.empId = p.empId ))
-- explain select p.name,p.empId from professor p where not exists (select * from teaching t,enrollment e where t.empId = p.empId and t.sem = e.sem and t.year = e.year and t.courseId = e.courseId and e.grade = 'S')

-- alter table enrollment drop index custom_course_index;
-- alter table enrollment drop index custom_sem_index;
-- alter table enrollment drop index custom_grade_index;
-- alter table enrollment drop index custom_year_index;

-- alter table enrollment add index custom_year_index (year);
-- alter table enrollment add index custom_sem_index (sem);
-- alter table enrollment add index custom_grade_index (grade);
-- alter table enrollment add index custom_course_index (courseId);

-- show index from enrollment 
