import { BaseEntity } from './../../shared';

export class Products implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public name?: string,
        public shtDescription?: string,
        public description?: string,
        public reorderLevel?: number,
        public reorderQuantity?: number,
        public averageMonthlyUsage?: number,
        public quantitySuppliedToDate?: number,
        public subCategory?: BaseEntity,
        public brand?: BaseEntity,
        public invRecords?: BaseEntity[],
        public suppliers?: BaseEntity,
        public receivedFromSupplier?: BaseEntity,
    ) {
    }
}
