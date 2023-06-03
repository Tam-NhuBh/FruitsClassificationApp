package hcmute.edu.vn.fruitsclassificationapp.model;

public class MainModel {
    String en_food_name;
    String fruit_name;
    String link_image;
    String vn_food_name;
    MainModel(){

    }
    public MainModel(String en_food_name, String fruit_name, String link_image, String vn_food_name) {
        this.en_food_name = en_food_name;
        this.fruit_name = fruit_name;
        this.link_image = link_image;
        this.vn_food_name = vn_food_name;
    }

    public String getEn_food_name() {
        return en_food_name;
    }

    public void setEn_food_name(String en_food_name) {
        this.en_food_name = en_food_name;
    }

    public String getFruit_name() {
        return fruit_name;
    }

    public void setFruit_name(String fruit_name) {
        this.fruit_name = fruit_name;
    }

    public String getLink_image() {
        return link_image;
    }

    public void setLink_image(String link_image) {
        this.link_image = link_image;
    }

    public String getVn_food_name() {
        return vn_food_name;
    }

    public void setVn_food_name(String vn_food_name) {
        this.vn_food_name = vn_food_name;
    }
}
