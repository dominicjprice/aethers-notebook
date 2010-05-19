package horizon.aether.sensors;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

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

	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(serviceClass.getName());
	}
	
	public Intent getIntent(Context context)
	{
		return new Intent(context, serviceClass);
	}
}
