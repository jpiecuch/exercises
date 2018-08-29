package pl.jakubpiecuch.exercises.service.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@DataObject
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Exercise {

    public Exercise(JsonObject jsonObject) {
        this.name = jsonObject.getString("name");
    }

    private String name;

    public JsonObject toJson() {
        return new JsonObject()
                .put("name", this.name);
    }
}
