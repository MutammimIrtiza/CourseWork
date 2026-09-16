import java.util.*;

// --- Mediator Interface ---
interface EmergencyMediator {
    void requestInvestigations(String patientId, List<String> tests);
    void submitResult(String patientId, String testType, String result);
}

// --- Colleague Component ---
abstract class MedicalUnit {
    protected EmergencyMediator mediator;
    protected String unitName;

    public MedicalUnit(EmergencyMediator mediator, String unitName) {
        this.mediator = mediator;
        this.unitName = unitName;
    }

    public String getUnitName() {
        return unitName;
    }
}

// --- Concrete Colleagues ---
class Doctor extends MedicalUnit {
    public Doctor(EmergencyMediator mediator) {
        super(mediator, "Doctor");
    }

    public void requestInvestigations(String patientId, List<String> tests) {
        mediator.requestInvestigations(patientId, tests);
    }

    public void notifyUrgent(String patientId, String testType, String result) {
        System.out.println("URGENT notification sent to Doctor.");
    }

    public void notifyCompletion(String patientId, Map<String, String> results) {
        System.out.println("Complete results sent to Doctor.");
    }
}

class PathologyLab extends MedicalUnit {
    public PathologyLab(EmergencyMediator mediator) {
        super(mediator, "PathologyLab");
    }

    public void receiveRequest(String patientId) {
        System.out.println("Pathology test requested for Patient " + patientId + ".");
    }

    public void processAndSubmit(String patientId, String result) {
        if ("CRITICAL".equalsIgnoreCase(result)) {
            System.out.println("Critical pathology result received for Patient " + patientId + ".");
        } else {
            System.out.println("Pathology result received for Patient " + patientId + ".");
        }
        mediator.submitResult(patientId, "Pathology", result);
    }
}

class RadiologyUnit extends MedicalUnit {
    public RadiologyUnit(EmergencyMediator mediator) {
        super(mediator, "RadiologyUnit");
    }

    public void receiveRequest(String patientId) {
        System.out.println("Radiology investigation requested for Patient " + patientId + ".");
    }

    public void processAndSubmit(String patientId, String result) {
        System.out.println("Radiology result received for Patient " + patientId + ".");
        mediator.submitResult(patientId, "Radiology", result);
    }
}

// --- Patient Record Tracker ---
class PatientRecord {
    private final String patientId;
    private final Set<String> requestedTests = new HashSet<>();
    private final Map<String, String> completedResults = new HashMap<>();

    public PatientRecord(String patientId, List<String> tests) {
        this.patientId = patientId;
        this.requestedTests.addAll(tests);
    }

    public void recordResult(String testType, String result) {
        completedResults.put(testType, result);
    }

    public boolean isComplete() {
        return completedResults.keySet().containsAll(requestedTests);
    }

    public Map<String, String> getCompletedResults() {
        return completedResults;
    }
}

// --- Concrete Mediator ---
class EmergencyCenter implements EmergencyMediator {
    private Doctor doctor;
    private PathologyLab pathologyLab;
    private RadiologyUnit radiologyUnit;

    private final Map<String, PatientRecord> patientTracker = new HashMap<>();

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPathologyLab(PathologyLab pathologyLab) {
        this.pathologyLab = pathologyLab;
    }

    public void setRadiologyUnit(RadiologyUnit radiologyUnit) {
        this.radiologyUnit = radiologyUnit;
    }

    @Override
    public void requestInvestigations(String patientId, List<String> tests) {
        patientTracker.put(patientId, new PatientRecord(patientId, tests));

        for (String test : tests) {
            if ("Pathology".equalsIgnoreCase(test) && pathologyLab != null) {
                pathologyLab.receiveRequest(patientId);
            } else if ("Radiology".equalsIgnoreCase(test) && radiologyUnit != null) {
                radiologyUnit.receiveRequest(patientId);
            }
        }
    }

    @Override
    public void submitResult(String patientId, String testType, String result) {
        PatientRecord record = patientTracker.get(patientId);
        if (record == null) return;

        record.recordResult(testType, result);

        // Urgent Result Handling
        boolean isUrgent = ("Pathology".equalsIgnoreCase(testType) && "CRITICAL".equalsIgnoreCase(result)) ||
                           ("Radiology".equalsIgnoreCase(testType) && "NOT OK".equalsIgnoreCase(result));

        if (isUrgent) {
            if (doctor != null) {
                doctor.notifyUrgent(patientId, testType, result);
            }
            System.out.println("URGENT notification sent to Patient " + patientId + ".");
        }

        // Complete Result Handling
        if (record.isComplete()) {
            System.out.println("All requested investigations completed for Patient " + patientId + ".");
            if (doctor != null) {
                doctor.notifyCompletion(patientId, record.getCompletedResults());
            }
            System.out.println("Complete results sent to Patient " + patientId + ".");
        }
    }
}

// --- Main Demonstration ---
public class Medical {
    public static void main(String[] args) {
        EmergencyCenter center = new EmergencyCenter();

        Doctor doctor = new Doctor(center);
        PathologyLab pathologyLab = new PathologyLab(center);
        RadiologyUnit radiologyUnit = new RadiologyUnit(center);

        center.setDoctor(doctor);
        center.setPathologyLab(pathologyLab);
        center.setRadiologyUnit(radiologyUnit);

        // Scenario: Doctor requests Pathology + Radiology for Patient P101
        doctor.requestInvestigations("P101", Arrays.asList("Pathology", "Radiology"));

        // Pathology returns CRITICAL first
        pathologyLab.processAndSubmit("P101", "CRITICAL");

        // Radiology returns OK later
        radiologyUnit.processAndSubmit("P101", "OK");
    }
}