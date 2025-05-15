package arq_hex_parking_access.application.ports.output;

public interface ContentManagerOutputPort {

	String saveImage(byte[] image);

	boolean compareImages(String contentManagerImageId, byte[] exitImage);

}
