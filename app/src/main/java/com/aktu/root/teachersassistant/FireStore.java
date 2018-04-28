package com.aktu.root.teachersassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import com.aktu.root.teachersassistant.student.StudentDashboard;
import com.aktu.root.teachersassistant.teacher.TeacherDashboard;
import com.aktu.root.teachersassistant.teacher.getuserinfo.GetStudentInfo;
import com.aktu.root.teachersassistant.teacher.getuserinfo.StudentDataModel;
import com.aktu.root.teachersassistant.teacher.post_file.FileDataModel;
import com.aktu.root.teachersassistant.teacher.post_file.ManageAssignment;
import com.aktu.root.teachersassistant.teacher.post_file.ManageNotes;
import com.aktu.root.teachersassistant.teacher.post_file.PostNotes;
import com.aktu.root.teachersassistant.utils.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 2/28/18.
 */

public class FireStore {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String string;
    private static StorageReference storage = FirebaseStorage.getInstance().getReference();

    public static void createProfile(Map<String, Object> student_Data, String user, final Context context) {
        db.collection(user.toLowerCase()).document(student_Data.get("email").toString())
                .set(student_Data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(context, "DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error writing document", e);
                        Toast.makeText(context, "Error writing document", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public static void loadStudentsDetails() {
        GetStudentInfo.allStudentDetails.clear();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("students").orderBy("roll_no", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }

                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                String rollNumber = change.getDocument().get("roll_no").toString();
                                String name2 = change.getDocument().get("full_name").toString();
                                String mobile_Number = change.getDocument().get("mobile_no").toString();
                                String email2 = change.getDocument().get("email").toString();
                                String daa = change.getDocument().get("daa").toString();
                                String os = change.getDocument().get("os").toString();
                                String mathematics = change.getDocument().get("mathematics").toString();
                                String compiler = change.getDocument().get("compiler").toString();
                                String dbms = change.getDocument().get("dbms").toString();
                                String network = change.getDocument().get("network").toString();
                                String automata = change.getDocument().get("automata").toString();
                                GetStudentInfo.allStudentDetails.add(new StudentDataModel(rollNumber, name2, mobile_Number, email2, daa, network, os, mathematics, dbms, compiler, automata));
//                                //Log.d(TAG, "New city:" + change.getDocument().getData());
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });
    }

    public static void loadAttendenceList() {
        loadStudentsDetails();
    }

    public static void postAttendence(ArrayList<StudentDataModel> student_Data, String subject, Context context) {

        String lecture;
        for (int i = 0; i < student_Data.size(); i++) {
            if (subject.equals("dbms")) {
                lecture = student_Data.get(i).getDbms1();
            } else if (subject.equals("mathematics")) {
                lecture = student_Data.get(i).getMathematics1();
            } else if (subject.equals("os")) {
                lecture = student_Data.get(i).getOs1();
            } else if (subject.equals("automata")) {
                lecture = student_Data.get(i).getAutomata1();
            } else if (subject.equals("network")) {
                lecture = student_Data.get(i).getNetwork1();
            } else if (subject.equals("daa")) {
                lecture = student_Data.get(i).getDaa1();
            } else {
                lecture = student_Data.get(i).getCompiler1();
            }

            String[] strings = lecture.split("/");
            int n = Integer.parseInt(strings[0]);
            int d = Integer.parseInt(strings[1]);
            if (student_Data.get(i).getCheckbox()) {
                n++;
            }
            d++;
            String str = n + "/" + d;

            db.collection("students").document(student_Data.get(i).getEmailId().toLowerCase().toString())
                    .update(subject, str);

            //Toast.makeText(context,""+n+"/"+t,Toast.LENGTH_SHORT).show();
        }
    }

    public static void postFile(String type, String filename, String fileUrl, String fileDescription) {
        Map<String, Object> map = new HashMap<>();
        map.put("filename", filename.trim());
        map.put("file_url", fileUrl.trim());
        map.put("description", fileDescription.trim());
        map.put("id", System.currentTimeMillis());

        db.collection(type).document(filename.toLowerCase().trim())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public static void loadAssignmentFilesList() {
        ManageAssignment.manageAssignmentList.clear();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("assignment").orderBy("id", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }
                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                String filename = change.getDocument().get("filename").toString();
                                String description = String.valueOf("Description : " + change.getDocument().get("description"));
                                String url = change.getDocument().get("file_url").toString();
                                Log.d(TAG, "New city:" + change.getDocument().getData());

                                ManageAssignment.manageAssignmentList.add(new FileDataModel(filename, description, url));
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);

                        }


                    }
                });
    }

    public static void loadNotesFilesList() {
        ManageNotes.manageNotesList.clear();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("notes").orderBy("id", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }
                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                String filename = change.getDocument().get("filename").toString();
                                String description = String.valueOf("Description : " + change.getDocument().get("description"));
                                String url = change.getDocument().get("file_url").toString();
                                Log.d(TAG, "New city:" + change.getDocument().getData());

                                ManageNotes.manageNotesList.add(new FileDataModel(filename, description, url));
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);

                        }


                    }
                });
    }

    public static void deleteFile(final String fileType, final String filename, final Context context) {
//delete file from fireStore
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("deleting...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        db.collection(fileType.toLowerCase()).document(filename)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
//delete file from srorage
                        StorageReference storageRef = storage;
                        StorageReference desertRef = storageRef.child(fileType.trim() + "/" + filename.trim());
                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        }, 4000);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error deleting document", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void checkUserType(String documentId, final Context context) {
        string = "";

        final DocumentReference docRef = db.collection("users").document(documentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        string = document.get("profile_type").toString();
                    } else {
                        Toast.makeText(context, "try again!", Toast.LENGTH_SHORT).show();
                        Login.mAuth.signOut();
                    }
                } else {
                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show();
                    Login.mAuth.signOut();
                }
            }
        });
        Toast.makeText(context, "Login as " + string, Toast.LENGTH_SHORT).show();
    }

    public static void showStudentDetails(final String documentId, final Context context) {
        final DocumentReference docRef = db.collection("students").document(documentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                        StudentDashboard.name.setText("Hi, " + document.get("full_name").toString());
                        StudentDashboard.rollNo.setText("Roll no. : " + document.get("roll_no").toString());
                        StudentDashboard.email.setText("Email : " + document.get("email").toString());
                        StudentDashboard.mobile.setText("Mobile no. : " + document.get("mobile_no").toString());
                        StudentDashboard.profileType.setText("Account Type : " + document.get("profile_type").toString());
                        String[] daa = document.get("daa").toString().split("/");
                        String[] os = document.get("os").toString().split("/");
                        String[] automata = document.get("automata").toString().split("/");
                        String[] dbms = document.get("dbms").toString().split("/");
                        String[] compiler = document.get("compiler").toString().split("/");
                        String[] mathematics = document.get("mathematics").toString().split("/");
                        String[] network = document.get("network").toString().split("/");

                        int present = Integer.parseInt(daa[0]) + Integer.parseInt(automata[0]) + Integer.parseInt(os[0]) + Integer.parseInt(dbms[0]) + Integer.parseInt(compiler[0]) + Integer.parseInt(mathematics[0]) + Integer.parseInt(network[0]);
                        int total = Integer.parseInt(daa[1]) + Integer.parseInt(automata[1]) + Integer.parseInt(os[1]) + Integer.parseInt(dbms[1]) + Integer.parseInt(compiler[1]) + Integer.parseInt(mathematics[1]) + Integer.parseInt(network[1]);

                        StudentDashboard.attendenceFraction.setText("(" + present + "/" + total + ")");
                        int percent = (int) ((Double.valueOf(present) / Double.valueOf(total)) * 100);
                        StudentDashboard.attendencePercent.setText(percent + "%");

//                        StudentDashboard.comiler.setText("Compiler : " + document.get("compiler").toString());
//                        StudentDashboard.dbms.setText("DBMS : " + document.get("dbms").toString());
//                        StudentDashboard.daa.setText("DAA : " + String.valueOf(document.get("daa")));
//                        StudentDashboard.network.setText("Comp. Network : " + String.valueOf(document.get("network")));
//                        StudentDashboard.automata.setText("Automata : " + String.valueOf(document.get("automata")));
//                        StudentDashboard.math.setText("Math : " + String.valueOf(document.get("mathematics")));
//                        StudentDashboard.os.setText("OS : " + String.valueOf(document.get("os")));
//                        StudentDashboard.attendenceTitle.setText("Attendence : " + present + "/" + total);
                        StudentDashboard.atten =  "Compiler : "+ document.get("compiler").toString() + "\n"+
                                "DBMS : "+document.get("dbms").toString() +"\n"+
                                "DAA : " +String.valueOf(document.get("daa")) +"\n"+
                                "Comp. Network : "+String.valueOf(document.get("network")) +"\n"+
                                "Automata : " +String.valueOf(document.get("automata")) +"\n"+
                                "Math : "+String.valueOf(document.get("mathematics")) +"\n"+
                                "OS : " +String.valueOf(document.get("os")) ;


                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(context, "try again!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "not found2", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public static void loadTeacherDetails(final String documentId, final Context context) {
        final DocumentReference docRef = db.collection("teachers").document(documentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                        TeacherDashboard.teacherName.setText("Hi ," + document.get("full_name").toString());
                        TeacherDashboard.teacherEmail.setText("Email : " + document.get("email").toString());
                        TeacherDashboard.teacherMobile.setText("Mobile : " + document.get("mobile_no").toString());

                    } else {
                        Log.d(TAG, "No such document");
                        Toast.makeText(context, "try again!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "not found2", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    public static void loadAdminDetails(final String documentId, final Context context) {

    }
}