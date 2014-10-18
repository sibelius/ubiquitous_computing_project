package ubiquitous.computing.behaviorcollection;

/**
 * This class group an image id and a description to be used by the CustomAdapter
 *
 * @author sibelius
 * Created by sibelius on 6/24/14.
 */
public class ImageText {

    private int description;
    private int imageId;

    public ImageText(int description, int imageId) {
        this.description = description;
        this.imageId = imageId;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

}
