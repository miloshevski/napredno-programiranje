//package lab3.aplikanti;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//class Faculty{
//    private String shortName;
//    private List<String> appropriateSubjects;
//    private List<StudyProgramme> studyProgrammes;
//
//    public Faculty(String shortName) {
//        this.shortName = shortName;
//        appropriateSubjects = new ArrayList<>();
//        studyProgrammes = new ArrayList<>();
//    }
//    public void addStudyProgramme(StudyProgramme s){
//        studyProgrammes.add(s);
//    }
//
//    public void addSubject(String s){
//        appropriateSubjects.add(s);
//    }
//
//    public String getShortName() {
//        return shortName;
//    }
//
//    public List<String> getAppropriateSubjects() {
//        return appropriateSubjects;
//    }
//
//    public List<StudyProgramme> getStudyProgrammes() {
//        return studyProgrammes;
//    }
//    private String subjects(){
//        StringBuilder sb = new StringBuilder();
//        String s = String.join(", ", appropriateSubjects);
//        return String.format("[%s]",s);
//    }
//
//    public long appropriateCountFor(Applicant a){
//        Set<String> set = new HashSet<>(appropriateSubjects);
//        return a.getSubjectsWithGrade()
//                .stream()
//                .filter(sw -> set.contains(sw))
//                .count();
//    }
//
//    private static double acceptPercent  (StudyProgramme sp){
//        int cap = sp.getEnrolledInPrivateQuota() + sp.getEnrolledInPublicQuota();
//        if(cap == 0){
//            return 0.0;
//        }
//        return (double) (100 * (sp.getEnrolledInPublicQuota() + sp.getEnrolledInPrivateQuota())) / cap;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(String.format("Faculty: %s%n",shortName));
//        sb.append(String.format("Subjects: %s%n",subjects()));
//        sb.append(String.format("Study Programmes:%n"));
//    }
//}
//
//class StudyProgramme{
//    private final String code;
//    private final String name;
//    private final int numPublicQuota;
//    private final int numPrivateQuota;
//    private int enrolledInPublicQuota;
//    private int enrolledInPrivateQuota;
//    private List<Applicant> applicants;
//    private final Faculty faculty;
//
//
//
//    public StudyProgramme(String code, String name, Faculty f, int numPublicQuota, int numPrivateQuota) {
//        this.code = code;
//        this.name = name;
//        this.numPublicQuota = numPublicQuota;
//        this.numPrivateQuota = numPrivateQuota;
//        this.enrolledInPublicQuota = 0;
//        this.enrolledInPrivateQuota = 0;
//        faculty = f;
//        applicants = new ArrayList<>();
//    }
//    public Faculty getFaculty(){
//        return this.faculty;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public String getName() {
//        return name;
//    }
//    public void addApplicant(Applicant a){
//        applicants.add(a);
//        if(enrolledInPublicQuota < numPublicQuota){
//            enrolledInPublicQuota++;
//        }else if(enrolledInPrivateQuota < numPrivateQuota){
//            enrolledInPrivateQuota++;
//        }
//    }
//    public int getEnrolledInPublicQuota() {
//        return enrolledInPublicQuota;
//    }
//
//    public int getEnrolledInPrivateQuota() {
//        return enrolledInPrivateQuota;
//    }
//
//    public double accepted(){
//        return ((double) (enrolledInPrivateQuota + enrolledInPublicQuota) / (numPrivateQuota + numPublicQuota)) * 100;
//    }
//
//    void calculateEnrollmentNumbers(){
//    }
//
//    @Override
//    public String toString() {
//        applicants.sort(Comparator.reverseOrder());
//        StringBuilder sb = new StringBuilder();
//        sb.append("Name: ").append(name).append("\n");
//        sb.append("Public Quota:\n");
//        for(int i =0;i<Math.min(applicants.size(), numPublicQuota);i++){
//            sb.append(this.applicants.get(i)).append("\n");
//        }
//        sb.append("Private Quota:\n");
//        for(int i=0;)
//    }
//}
//
//class Applicant implements Comparable<Applicant>{
//    private int id;
//    private String name;
//    private double gpa;
//    private List<SubjectWithGrade> subjectsWithGrade;
//    private StudyProgramme studyProgramme;
//
//    public Applicant(int id, String name, double gpa, StudyProgramme studyProgramme) {
//        this.id = id;
//        this.name = name;
//        this.gpa = gpa;
//        this.studyProgramme = studyProgramme;
//        subjectsWithGrade = new ArrayList<>();
//    }
//
//
//    public int getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public double getGpa() {
//        return gpa;
//    }
//
//    public List<SubjectWithGrade> getSubjectsWithGrade() {
//        return subjectsWithGrade;
//    }
//
//    public StudyProgramme getStudyProgramme() {
//        return studyProgramme;
//    }
//
//    public void addSubjectAndGrade(String subject, int grade){
//        SubjectWithGrade s = new SubjectWithGrade(subject, grade);
//        subjectsWithGrade.add(s);
//    }
//    private boolean isAppropriate(SubjectWithGrade s){
//        Faculty f = studyProgramme.getFaculty();
//        return f.getAppropriateSubjects().contains(s.getSubject());
//    }
//
//    public double calculatePoints(){
//        double p = 0;
//        p+=gpa*12;
//        for(SubjectWithGrade s : subjectsWithGrade){
//            p += s.getGrade() * (isAppropriate(s) ? 2 : 1.2);
//        }
//        return p;
//    }
//
//    @Override
//    public int compareTo(Applicant o) {
//        return Double.compare(calculatePoints(), o.calculatePoints());
//    }
//
//    @Override
//    public String toString(){
//        return "Id: " + id + ", Name: " + name + ", GPA: " +
//                gpa + " - " + calculatePoints();
//    }
//}
//
//class SubjectWithGrade
//{
//    private String subject;
//    private int grade;
//    public SubjectWithGrade(String subject, int grade) {
//        this.subject = subject;
//        this.grade = grade;
//    }
//    public String getSubject() {
//        return subject;
//    }
//    public int getGrade() {
//        return grade;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof SubjectWithGrade)) return false;
//        SubjectWithGrade that = (SubjectWithGrade) o;
//        return Objects.equals(subject, that.subject);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(subject);
//    }
//
//    @Override
//    public String toString() {
//        return subject;
//    }
//}
//
//class Enrollment{
//    private Applicant applicant;
//    private StudyProgramme studyProgramme;
//
//    public Enrollment(Applicant applicant, StudyProgramme programme){
//        this.applicant = applicant;
//        this.studyProgramme = programme;
//    }
//
//    public Applicant getApplicant() {
//        return applicant;
//    }
//
//    public StudyProgramme getStudyProgramme() {
//        return studyProgramme;
//    }
//}
//
//class EnrollmentsIO {
//    public static void printRanked(List<Faculty> faculties) {
//        String out = faculties.stream()
//                .map(Faculty::toString)
//                .collect(Collectors.joining(System.lineSeparator()));
//        System.out.print(out);
//    }
//
//    public static List<Enrollment> readEnrollments(List<StudyProgramme> studyProgrammes, InputStream inputStream) {
//        List<Enrollment> result = new ArrayList<>();
//        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//
//        br.lines().forEach(line -> {
//            if (line == null) return;
//            String trimmed = line.trim();
//            if (trimmed.isEmpty()) return;
//
//            String[] parts = trimmed.split(";");
//            if (parts.length < 7) return; // минимална валидација
//
//            int id = Integer.parseInt(parts[0].trim());
//            String name = parts[1].trim();
//            double gpa = Double.parseDouble(parts[2].trim());
//
//            String programmeCode = parts[parts.length - 1].trim();
//
//            StudyProgramme prog = studyProgrammes.stream()
//                    .filter(sp -> sp.getCode().equals(programmeCode))
//                    .findFirst().orElse(null);
//            if (prog == null) return; // или фрли IllegalArgumentException
//
//            Applicant applicant = new Applicant(id, name, gpa, prog);
//
//            for (int i = 3; i < parts.length - 1; i++) {
//                String token = parts[i].trim();
//                if (token.isEmpty()) continue;
//                String[] sg = token.split(":");
//                if (sg.length != 2) continue;
//                String subj = sg[0].trim();
//                int grade = Integer.parseInt(sg[1].trim());
//                applicant.addSubjectAndGrade(subj, grade);
//            }
//
//            prog.addApplicant(applicant);
//            result.add(new Enrollment(applicant, prog));
//        });
//        return result;
//    }
//}
//
//public class EnrollmentsTest {
//
//    public static void main(String[] args) {
//        Faculty finki = new Faculty("FINKI");
//        finki.addSubject("Mother Tongue");
//        finki.addSubject("Mathematics");
//        finki.addSubject("Informatics");
//
//        Faculty feit = new Faculty("FEIT");
//        feit.addSubject("Mother Tongue");
//        feit.addSubject("Mathematics");
//        feit.addSubject("Physics");
//        feit.addSubject("Electronics");
//
//        Faculty medFak = new Faculty("MEDFAK");
//        medFak.addSubject("Mother Tongue");
//        medFak.addSubject("English");
//        medFak.addSubject("Mathematics");
//        medFak.addSubject("Biology");
//        medFak.addSubject("Chemistry");
//
//        StudyProgramme si = new StudyProgramme("SI", "Software Engineering", finki, 4, 4);
//        StudyProgramme it = new StudyProgramme("IT", "Information Technology", finki, 2, 2);
//        finki.addStudyProgramme(si);
//        finki.addStudyProgramme(it);
//
//        StudyProgramme kti = new StudyProgramme("KTI", "Computer Technologies and Engineering", feit, 3, 3);
//        StudyProgramme ees = new StudyProgramme("EES", "Electro-energetic Systems", feit, 2, 2);
//        feit.addStudyProgramme(kti);
//        feit.addStudyProgramme(ees);
//
//        StudyProgramme om = new StudyProgramme("OM", "General Medicine", medFak, 6, 6);
//        StudyProgramme nurs = new StudyProgramme("NURS", "Nursing", medFak, 2, 2);
//        medFak.addStudyProgramme(om);
//        medFak.addStudyProgramme(nurs);
//
//        List<StudyProgramme> allProgrammes = new ArrayList<>();
//        allProgrammes.add(si);
//        allProgrammes.add(it);
//        allProgrammes.add(kti);
//        allProgrammes.add(ees);
//        allProgrammes.add(om);
//        allProgrammes.add(nurs);
//
//        EnrollmentsIO.readEnrollments(allProgrammes, System.in);
//
//        List<Faculty> allFaculties = new ArrayList<>();
//        allFaculties.add(finki);
//        allFaculties.add(feit);
//        allFaculties.add(medFak);
//
//        allProgrammes.stream().forEach(StudyProgramme::calculateEnrollmentNumbers);
//
//        EnrollmentsIO.printRanked(allFaculties);
//
//    }
//
//
//}
