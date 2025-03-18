package com.example.demo.enumeration;

public enum InventoryStatus {
    INSTOCk("INSTOCK"),
    LOWSTOCK("LOWSTOCK"),
    OUTOFSTOCK("OUTOFSTOCK");

    private final String status;

    InventoryStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static InventoryStatus fromString(String status) {
        if (status == null || status.isEmpty()) {
            return null;
        }

        for (InventoryStatus inventoryStatus : InventoryStatus.values()) {
            if (inventoryStatus.getStatus().equalsIgnoreCase(status)) {
                return inventoryStatus;
            }
        }
        return null;
    }
}
