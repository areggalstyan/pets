package com.aregcraft.pets.json;

import com.aregcraft.delta.api.json.annotation.JsonAdapterFor;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.lang.reflect.Type;

@JsonAdapterFor(Expression.class)
public class ExpressionDeserializer implements JsonDeserializer<Expression> {
    @Override
    public Expression deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        var expression = new Expression(json.getAsString());
        expression.addArguments(new Argument("x"));
        return expression;
    }
}
