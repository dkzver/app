package com.wearetogether.v2.ui.holders.group;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import org.jetbrains.annotations.NotNull;

public class HolderItemGroup extends HolderBaseGroup {

    public HolderItemGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
    }

    @Override
    public void bind(DataGroup item) {

    }

    protected Spanned BuildUserDescription(FragmentActivity activity, DataGroup item) {
        String description = "";
//        description += StringAppended(description, "<strong>Distance</strong> <span>" + item.distance + "</span>");
        if (item.title != null && !item.title.equals("")) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_full_name) + "</strong> <span>" + item.title + "</span>");
        }
        if(item.show_age == 1) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_age) + "</strong> <span>" + item.age + "</span>");
        }
        if(item.show_sex == 1) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_sex) + "</strong> <span>" + item.sex + "</span>");
        }
        if(item.location != null && !item.location.equals("")) {
            String location = String.format("%s, %s", item.country, item.city);
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_location) + "</strong> <span>" + location + "</span>");
        }
//        if(item.array_interests != null && item.array_interests.length > 0) {
//            String interests = item.array_interests[0];
//            for(int x = 1; x < item.array_interests.length; x++) {
//                interests += ", " + item.array_interests[x];
//            }
//            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_interests) + "</strong> <span>" + interests + "</span>");
//        }

        if (description.equals("")) {
            return null;
        } else return fromHtml(description);
    }

    protected Spanned BuildPlaceDescription(FragmentActivity activity, DataGroup item) {
        String description = "";
//        description += StringAppended(description, "<strong>visiters</strong> <span>" + item.visiters.size() + "</span>");
        if (item.description != null && !item.description.equals("")) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_description) + "</strong> <span>" + item.description + "</span>");
        }
        if (item.anonymous_visit == 1) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_anonymous_visit) + "</strong> <span>" + activity.getString(R.string.positive) + "</span>");
        } else {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_anonymous_visit) + "</strong> <span>" + activity.getString(R.string.negative) + "</span>");
        }
        if (item.time_visit != null && !item.time_visit.equals("")) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_time_visit) + "</strong> <span>" + item.time_visit + "</span>");
        }
        String date = "";
        if (item.date_begin != null && !item.date_begin.equals("")) {
            date = item.date_begin;
        }
        if (item.date_end != null && !item.date_end.equals("")) {
            if (date.equals("")) {
                date = item.date_end;
            } else {
                date = " - " + item.date_end;
            }
        }
        if (!date.equals("")) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_date) + "</strong> <span>" + date + "</span>");
        }
        if (item.count_participant == 0) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_count_participant) + "</strong> <span>" + activity.getString(R.string.unlimited) + "</span>");
        } else {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_count_participant) + "</strong> <span>" + item.count_participant + "</span>");
        }
        String category = "";
        if (adapterGroup.array_categories != null && item.category_id < adapterGroup.array_categories.length) {
            category = adapterGroup.array_categories[item.category_id];
        }
        if (!category.equals("")) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_category) + "</strong> <span>" + category + "</span>");
        }

        if (item.disable_comments == 1) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_disable_comments) + "</strong> <span>" + activity.getString(R.string.positive) + "</span>");
        } else {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_disable_comments) + "</strong> <span>" + activity.getString(R.string.negative) + "</span>");
        }

        if (item.only_for_friends == 1) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_only_for_friends) + "</strong> <span>" + activity.getString(R.string.positive) + "</span>");
        } else {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_only_for_friends) + "</strong> <span>" + activity.getString(R.string.negative) + "</span>");
        }
        if (item.rating == 0) {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_like) + "</strong> <span>" + activity.getString(R.string.negative) + "</span>");
        } else {
            description += StringAppended(description, "<strong>" + activity.getString(R.string.label_like) + "</strong> <span>" + item.rating + "</span>");
        }
//        description += StringAppended(description, "<strong>Visit: </strong> <span>" + item.visit + "</span>");
//        description += StringAppended(description, "<strong>Vote: </strong> <span>" + item.vote + "</span>");
//        description += StringAppended(description, "<strong>Save: </strong> <span>" + item.save + "</span>");
        if (item.location != null && !item.location.equals("")) {
            description += StringAppended(description, "<p><strong>" + activity.getString(R.string.label_location) + "</strong> <span>" + item.location + "</span></p>");
        }
        if (description.equals("")) {
            return null;
        } else return fromHtml(description);
    }

    protected static Spanned fromHtml(String value) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            return Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY);
//        } else {
//            return Html.fromHtml(value);
//        }
        return Html.fromHtml(value);
    }

    protected static String StringAppended(String description, String value) {

        String text = "";
        if (description.equals("")) {
            text += value + ".";
        } else {
            text += " " + value + ".";
        }
        return text;
    }
}
