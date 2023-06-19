package com.aregcraft.pets.json;

import com.aregcraft.delta.api.json.annotation.JsonAdapterFor;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.lang.reflect.Type;

@JsonAdapterFor(Expression.class)
public class ExpressionDeserializer implements JsonDeserializer<Expression> {
    @Override
    public Expression deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return new ExpressionBuilder(json.getAsString()).variable("x").build();
    }
}
