/*
 * Name: $RCSfile: NetParameter.java,v $
 * Version: $Revision: 1.6 $
 * Date: $Date: 2010/12/06 02:44:54 $
 *
 */
package com.appolis.network;

/**
 * @author HoangNH11
 */
public class NetParameter
{
    private String name;
    private String value;

    /**
     * Default Constructor.
     */
    public NetParameter()
    {}

    public NetParameter(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public NetParameter(NetParameter obj)
    {
        this.name = obj.name;
        this.value = obj.value;
    }

    /**
     * Get the value of name.
     * 
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the value for name.
     * 
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the value of value.
     * 
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Set the value for value.
     * 
     * @param value the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof NetParameter)
        {
            NetParameter netParameter = (NetParameter) obj;

            return (this.name.equals(netParameter.name) && this.value.equals(netParameter.value));
        }
        else
        {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result = name.hashCode();
        result = 20 * result + value.hashCode();
        return result;
    }
}
