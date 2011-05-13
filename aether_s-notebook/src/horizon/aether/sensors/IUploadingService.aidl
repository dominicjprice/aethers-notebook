package horizon.aether.sensors;

interface IUploadingService 
{
	boolean uploadFile(in String filePath, in String serverUrl);
}