package sleeping_vityaz.onerm.records_util;

/**
 * Created by naja-ox.
 */
public class RecordObject {

    public int recordsID;
    public double weight;
    public int reps;
    public double oneRM;
    public String date;

    public RecordObject() {
    }

    public RecordObject(int recordsID, double weight, int reps, double oneRM, String date) {
        this.recordsID = recordsID;
        this.weight = weight;
        this.reps = reps;
        this.oneRM = oneRM;
        this.date = date;
    }

    public int getRecordsID() {
        return recordsID;
    }

    public double getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public double getOneRM() {
        return oneRM;
    }

    public String getDate() {
        return date;
    }

    public void setRecordsID(int recordsID) {
        this.recordsID = recordsID;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setOneRM(double oneRM) {
        this.oneRM = oneRM;
    }

    public void setDate(String date) {
        this.date = date;
    }
}