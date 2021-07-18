package persistence.modelmapper;

public class ModelMapper implements IModelMapper {


    @Override
    public org.modelmapper.ModelMapper getMapper() {
        return new org.modelmapper.ModelMapper();
    }
}
