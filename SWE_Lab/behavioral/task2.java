
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


enum Status {
    REGISTERED, DEPT_CONFIRMED, OFFICE_ORDER_ISSUED, TESTIMONIAL_ISSUED, COMPLETED
}

class Student {
    private int id;
    private static int cnt = 0;
    public Student() {
        this.id = ++cnt;
    }
    public int getId() {return this.id;}
    public void notify(String message) {
        System.out.println("[Notifying ID" + id + " : " + message);
    }
}

abstract class Office {
    protected Coordinator coordinator;
    public Office(Coordinator c) {this.coordinator = c;}
}

class DeptOffice extends Office {
    public DeptOffice(Coordinator c) {
        super(c);
    }
    public void confirmAcademicCompletion(Student s) {
        System.out.println("[Dept Office] Confirming academic completion for Student ID: " + s.getId());
        coordinator.confirmAcademicCompletion(s);
    }
}

class ControllerOfExam extends Office {
    public ControllerOfExam(Coordinator c) {
        super(c);
    }
    public void publishResultOrder(Student s) {
        System.out.println("[Controller] Attempting to publish office order for Student ID: " + s.getId());
        coordinator.publishResultOrder(s);
    }
    public void issueCertificateAndTranscript(Student s) {
        System.out.println("[Controller] Attempting to issue certificate for Student ID: " + s.getId());
        coordinator.issueCertificateAndTranscript(s);
    }
}

class DSW extends Office {
    public DSW(Coordinator c) {
        super(c);
    }
    public void issueTestimonial(Student s) {
        System.out.println("[DSW] Attempting to issue Testimonial for Student ID: " + s.getId());
        coordinator.issueTestimonial(s);
    }
}

class Coordinator {
    private DeptOffice deptOffice;
    private ControllerOfExam examController;
    private DSW dsw;

    private List<Student> students = new ArrayList<>();
    private Map<Integer, Status> status = new HashMap<>();
    public Coordinator() {

    }

    public void registerDeptOffice(DeptOffice o) {this.deptOffice = o;}
    public void registerControllerOfExam(ControllerOfExam o) {this.examController = o;}
    public void registerDSW(DSW o) {this.dsw = o;}

    public void registerStudent(Student s) {
        students.add(s);
        status.put(s.getId(), Status.REGISTERED);
        System.out.println("[Coordinator] Registered Student ID : " + s.getId());
    }

    public void confirmAcademicCompletion(Student s) {
        if(status.containsKey(s.getId())) {
            if(status.get(s.getId()) == Status.REGISTERED) {
                status.put(s.getId(), Status.DEPT_CONFIRMED);
                System.out.println("[Coordinator] Departmental confirmation recorded for Student ID: " + s.getId());
                s.notify("Departmental confirmation recorded.");
            }
        }
    }

    public void publishResultOrder(Student s) {
        if(!status.containsKey(s.getId())) return;
        if(status.get(s.getId()) != Status.DEPT_CONFIRMED) {
            System.out.println("[Coordinator] Failed to issue office order. Dept confirmation missing.");
            return;
        }
        status.put(s.getId(), Status.OFFICE_ORDER_ISSUED);
        System.out.println("[Coordinator] Successfully issued office order");
        s.notify("Office order issued.");
    }

    public void issueTestimonial(Student s) {
        if(!status.containsKey(s.getId())) return;
        if(status.get(s.getId()) != Status.OFFICE_ORDER_ISSUED) {
            System.out.println("[Coordinator] Failed to issue testimonial. Office order issue missing.");
            return;
        }
        status.put(s.getId(), Status.TESTIMONIAL_ISSUED);
        System.out.println("[Coordinator] Successfully issued testimonial");
        s.notify("Testimonial issued.");
    }

    public void issueCertificateAndTranscript(Student s) {
        if(!status.containsKey(s.getId())) return;
        if(status.get(s.getId()) != Status.TESTIMONIAL_ISSUED) {
            System.out.println("[Coordinator] Failed to issue certificate. Testimonial missing.");
            return;
        }
        status.put(s.getId(), Status.COMPLETED);
        System.out.println("[Coordinator] Successfully issued certificate and transcript");
        s.notify("Issued certificate and transcript");
    }

    void showStatus(Student s) {
        if(!status.containsKey(s.getId())) return;
        System.out.println("[Status] Student ID : " + s.getId() + " : " + status.get(s.getId()) );
    }

}

public class task2 {
    public static void main(String[] args) {
        Coordinator coordinator = new Coordinator();
        DeptOffice deptOffice = new DeptOffice(coordinator);
        ControllerOfExam examController = new ControllerOfExam(coordinator);
        DSW dsw = new DSW(coordinator);
        coordinator.registerDeptOffice(deptOffice);
        coordinator.registerControllerOfExam(examController);
        coordinator.registerDSW(dsw);

        Student s1 = new Student();

        Student s2 = new Student();
        coordinator.registerStudent(s2);
        coordinator.showStatus(s2);

        examController.publishResultOrder(s2);
        deptOffice.confirmAcademicCompletion(s2);
        coordinator.showStatus(s2);

        examController.issueCertificateAndTranscript(s2);
        coordinator.showStatus(s2);

        examController.publishResultOrder(s2);
        coordinator.showStatus(s2);

        examController.issueCertificateAndTranscript(s2);

        dsw.issueTestimonial(s2);
        examController.issueCertificateAndTranscript(s2);
        coordinator.showStatus(s2);


    }
}
