package co.edu.hotel.cleinteservice.domain;

public enum DocumentType {
    CC("Cédula de ciudadanía"),
    CE("Cédula de extranjería"),
    NIT("NIT"),
    PASSPORT("Pasaporte"),
    PPT("Permiso por protección temporal");

    private final String description;

    DocumentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name();
    }

    public static DocumentType fromValue(String value) {
        if (value == null) return null;
        try {
            return DocumentType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}