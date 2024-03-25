package is.hi.afk6.hbv2.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A Serializer and Deserializer for converting LocalDate to and from JSON.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 01/03/2024
 * @version 1.0
 */
public class LocalDateSerializer implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate>
{
    // Formatter to use.
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Serializes a LocalDate object to Json.
     *
     * @param date      The object that needs to be converted to Json.
     * @param typeOfSrc The actual type (fully genericized version) of the source object.
     * @param context   The context of the serialization.
     *
     * @return A JsonElement object representing the given LocalDate object.
     */
    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context)
    {
        return new JsonPrimitive(formatter.format(date));
    }

    /**
     * Deserializes a JsonElement to a LocalDate object.
     *
     * @param json    The Json data being deserialized
     * @param typeOfT The type of the Object to deserialize to
     * @param context The context of the deserialization
     *
     * @return A LocalDate object representing the given JsonElement.
     *
     * @throws JsonParseException If the JsonElement is not a valid LocalDate.
     */
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString(), formatter);
    }
}
