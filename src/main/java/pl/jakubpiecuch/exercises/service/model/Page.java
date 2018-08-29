package pl.jakubpiecuch.exercises.service.model;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@DataObject
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Page {

    public Page(JsonObject jsonObject) {
        this.items = jsonObject.getJsonArray("items")
                .stream()
                .map(item -> new Exercise((JsonObject) item))
                .collect(Collectors.toList());
    }

    List<Exercise> items;

    public JsonObject toJson() {
        return new JsonObject()
                .put("items", new JsonArray(items.stream().map(Exercise::toJson).collect(Collectors.toList())));
    }
}
