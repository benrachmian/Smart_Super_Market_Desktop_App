package SDMSystemDTO.discount;

public enum DiscountKind {
    IRRELEVANT{
        @Override
        public String toString() {
            return "Irrelevant";
        }
    },
    ONE_OF{
        @Override
        public String toString() {
            return "One of";
        }
    },
    ALL_OR_NOTHING{
        @Override
        public String toString() {
            return "All or nothing";
        }
    }
}



