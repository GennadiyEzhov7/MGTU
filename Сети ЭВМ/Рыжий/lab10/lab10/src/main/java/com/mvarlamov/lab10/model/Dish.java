package com.mvarlamov.lab10.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dish {
    private int id;
    private @NotNull String title;
    private @NotNull String anons;
    private @NotNull String text;
    private List<Tag> tags;
    private List<Comment> comments;
    @JsonSerialize(using = FormatDateSerializer.class)
    private Date dateTime;
    private MultipartFile image;
    private String imagePath;

    public Dish(){}

    public void setDateTime(long dateTime) {
        this.dateTime = new Date(dateTime);
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setTags(@Nullable String tags) {
        if (tags != null) {
            List<Tag> res = new ArrayList<>();
            for (String tag : tags.split(",")) {
                Tag t = new Tag();
                t.setText(tag);
                res.add(t);
            }
            this.tags = res;
        }
    }

    public void setTagsAsList(@Nullable List<Tag> tags) {
        if (tags != null)
            this.tags = tags;
    }

    private static class FormatDateSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(new SimpleDateFormat("HH:mm dd.MM.yyyy").format(value.getTime()));
        }
    }
}
