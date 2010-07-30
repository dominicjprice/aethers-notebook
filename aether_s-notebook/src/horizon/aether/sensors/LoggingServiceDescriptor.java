package horizon.aether.sensors;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Descriptor for the sensor services to hold the name, description and the 
 * class of a logging service.
 * 
 * The class also implements Parcelable so that LoggingServiceDescriptor objects 
 * can be send from one process to another through an AIDL interface.
 */
public class LoggingServiceDescriptor
implements Parcelable
{
	public final String name;
	
	public final String description;
	
	public final Class<?> serviceClass;
	
	public static final Parcelable.Creator<LoggingServiceDescriptor> CREATOR 
			= new Parcelable.Creator<LoggingServiceDescriptor>() 
	{
        public LoggingServiceDescriptor createFromParcel(Parcel in) 
        {
            return new LoggingServiceDescriptor(in);
        }

        public LoggingServiceDescriptor[] newArray(int size) 
        {
            return new LoggingServiceDescriptor[size];
        }
    };

    /**
     * Constructor.
     * @param name
     * @param description
     * @param serviceClass
     */
	public LoggingServiceDescriptor(String name, String description, Class<?> serviceClass)
	{
		this.name = name;
		this.description = description;
		this.serviceClass = serviceClass;
	}
	
	private LoggingServiceDescriptor(Parcel in)
	{
		this.name = in.readString();
		this.description = in.readString();
		try 
		{
			this.serviceClass = Class.forName(in.readString());
		}
		catch(ClassNotFoundException e) 
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * Describes the kinds of special objects contained in this Parcelable's 
	 * marshalled representation.
	 */
	@Override
	public int describeContents() 
	{
		return 0;
	}

	/**
	 * Flattens this object in to a Parcel.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(serviceClass.getName());
	}
	
	/**
	 * Returns the service intent.
	 * @param context
	 * @return
	 */
	public Intent getIntent(Context context)
	{
		return new Intent(context, serviceClass);
	}
}
