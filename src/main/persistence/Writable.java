package persistence;

import org.json.JSONObject;

// ACKNOWLEDGEMENT: The codes below are based on the on the ones in JsonSerializationDemo

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
