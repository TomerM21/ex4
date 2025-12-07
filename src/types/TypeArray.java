package types;

public class TypeArray extends Type
{
    public Type elementType;

    public TypeArray(String name, Type elementType)
    {           
        this.name = name;
        this.elementType = elementType;
    }
    
    @Override
    public boolean isArray(){ return true;}
    
}
