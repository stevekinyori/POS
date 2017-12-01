import { BaseEntity } from './../../shared';

export class Inventory implements BaseEntity {
    constructor(
        public id?: number,
        public batchNo?: string,
        public barCodeNo?: string,
        public quantity?: number,
        public soldToDate?: number,
        public availableItems?: number,
        public unitSellingPrice?: number,
        public unitBuyingPrice?: number,
        public products?: BaseEntity,
        public transactions?: BaseEntity[],
    ) {
    }
}
