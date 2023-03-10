package pricetracker;

import java.util.Optional;

public interface ImageSaver {

    /**
     * Saves Image using passed URL as a String, and returns a relative path to the saved image.
     * <b>Note:</b> adds some symbols to file name, to avoid file override if a file with such name already exists.
     *
     * @param url     an image URL as a String
     *                (example https://avic.ua/assets/cache/images/assets/files/products/8995/image/450x400-apple-iphone-5s-gray-12.jpg")
     * @param saveDir directory where to save (example "webapp/src/main/resources/static/assets/productImages")
     * @return an Optional<String> with a relative path to the saved image. If any error occurs, Optional will be empty.
     * For example, for parameters above result will be
     * "webapp/src/main/resources/static/assets/productImages/450x400-apple-iphone-5s-gray-12_SOME_HASH_HERE.jpg".
     */
    Optional<String> saveImageByUrl(String url, String saveDir);

    /**
     * The method does the same with Optional<String> saveImageByUrl(String url, String saveDir),
     * but with one difference: it not requires a save directory path, cause it uses default path.
     *
     * @param url an image URL as a String
     * @return an Optional<String> with a relative path to the saved image. If any error occurs, Optional will be empty.
     */
    Optional<String> saveImageByUrlToDefaultFolder(String url);

    /**
     * Returns path to default save folder
     *
     * @return path to default save folder
     */
    String getDefaultFolderPath();
}
