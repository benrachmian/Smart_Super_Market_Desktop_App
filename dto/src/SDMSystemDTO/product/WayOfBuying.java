package SDMSystemDTO.product;

public enum WayOfBuying {
    BY_QUANTITY {
        @Override
        public String toString() {
            return "By quantity";
        }
    }, BY_WEIGHT{
        @Override
        public String toString() {
            return "By weight";
        }
    }
}
