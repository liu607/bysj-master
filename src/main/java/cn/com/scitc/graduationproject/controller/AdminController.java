package cn.com.scitc.graduationproject.controller;

import cn.com.scitc.graduationproject.dao.*;
import cn.com.scitc.graduationproject.model.Course;
import cn.com.scitc.graduationproject.model.Pjclass;
import cn.com.scitc.graduationproject.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class AdminController {
    @Autowired
    CourseDao courseDao;
    @Autowired
    ClassDao classDao;
    @Autowired
    UsersDao usersDao;
    @Autowired
    ExamDao examDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    PaperDao paperDao;
    @Autowired
    StudentexamDao studentexamDao;
    @RequestMapping("/AdminManage")
    private String AdminManage(){
        return "admin/AdminManage";
    }

    @RequestMapping("/AdminCourseManage")
    private String AdminCourseManage(Model model){
        Iterable<Course> courselist = courseDao.findAll();
        model.addAttribute("list",courselist);
        return "admin/CourseList";
    }
    @ResponseBody
    @RequestMapping("/findCourseByCno")
    private Course findExamByEid(@RequestBody Course courses){
        Course byCno = courseDao.findByCno(courses.getCno());
        if (byCno!= null) {
            return byCno;
        } else {
            return null;
        }
    }

    @RequestMapping("/updateCourse")
    private String updateCourse(Course course ){
        courseDao.save(course);
        return "redirect:/AdminCourseManage";
    }

    @RequestMapping("/addCourse")
    private String addCourse(String cname){
        Course course = new Course();
        course.setCname(cname);
        courseDao.save(course);
        return "redirect:/AdminCourseManage";
    }

    @RequestMapping("/deleteCourse")
    private String deleteCourse(Integer cno){
        paperDao.deleteByCno(cno);
        subjectDao.deleteByCno(cno);
        examDao.deleteByCno(cno);
        courseDao.deleteByCno(cno);
        return "redirect:/AdminCourseManage";
    }

    @RequestMapping("/AdminPjclassManage")
    private  String AdminPjclassManage(Model model){
        Iterable<Pjclass> pjclasses = classDao.findAll();
        model.addAttribute("list",pjclasses);
        return "admin/ClassList";
    }
    @ResponseBody
    @RequestMapping("/findClassByClassid")
    private Pjclass findClassByClassid(@RequestBody Pjclass pjs){
        Pjclass byClassid = classDao.findByClassid(pjs.getClassid());
        if (byClassid!= null) {
            return byClassid;
        } else {
            return null;
        }
    }

    @RequestMapping("/updateClass")
    private String updateClass(Pjclass pjclass ){
        classDao.save(pjclass);
        return "redirect:/AdminPjclassManage";
    }

    @RequestMapping("/addClass")
    private String addClass(String classname){
        Pjclass pjclass = new Pjclass();
        pjclass.setClassname(classname);
        classDao.save(pjclass);
        return "redirect:/AdminPjclassManage";
    }

    @RequestMapping("/deleteClass")
    private String deleteClass(Integer classid){
        studentexamDao.deleteByClassid(classid);
        examDao.deleteByClassid(classid);
        usersDao.deleteByClassid(classid);
        classDao.deleteByClassid(classid);
        return "redirect:/AdminPjclassManage";
    }

    @RequestMapping("/AdminTeacherManage")
    private String AdminTeacherManage(Model model,Integer pageNum){
        if (pageNum == null){
            pageNum = 1;
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "userid");  // ?????????"recordNo"?????????????????????????????????????????????????????????????????????????????????????????????
        Pageable pageable = PageRequest.of(pageNum - 1, 5, sort);
        Page<Users> teachers = usersDao.findTeacher(1, pageable);
        model.addAttribute("teachers",teachers);
        return "admin/TeacherList";
    }

    @ResponseBody
    @RequestMapping("/TeacherEdit")
    private Users TeacherEdit(@RequestBody Users users){
        Users user = usersDao.findByUserid(users.getUserid());
        if (user!= null) {
            return user;
        } else {
            return null;
        }
    }

    @RequestMapping("/updateTeacher")
    private String updateTeacher(Users users ){
        usersDao.save(users);
        return "redirect:/AdminTeacherManage";
    }

    @RequestMapping("/addTeacher")
    private String addTeacher(Integer roleid,String username,String userpwd,String truename,Integer classid){
        Users users = new Users();
        users.setRoleid(roleid);
        users.setUsername(username);
        users.setUserpwd(userpwd);
        users.setTruename(truename);
        users.setClassid(classid);
        usersDao.save(users);
        return "redirect:/AdminTeacherManage";
    }

    @RequestMapping("/deleteTeacher")
    private String deleteTeacher(Integer userid){

        usersDao.deleteByUserid(userid);
        return "redirect:/AdminCourseManage";
    }
}
