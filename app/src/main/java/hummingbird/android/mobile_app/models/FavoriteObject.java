package hummingbird.android.mobile_app.models;

/**
 * Created by CzeWen on 2015-12-20.
 */
public class FavoriteObject {
    private int id;
    private int user_id;
    private String item_type;
    private String created_at;
    private String updated_at;
    private int fav_rank;


    public int getId(){
        return id;
    }

    public int user_id(){
        return user_id;
    }

    public String getItemType(){
        return item_type;
    }

    public String created_at(){
        return created_at;
    }

    //public Date updated_at(){
    //	return updated_at;
    //}

    public int getFavRank(){
        return fav_rank;
    }
}
