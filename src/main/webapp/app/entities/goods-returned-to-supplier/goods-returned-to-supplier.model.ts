import { BaseEntity } from './../../shared';

export class GoodsReturnedToSupplier implements BaseEntity {
    constructor(
        public id?: number,
        public batchNo?: string,
        public quantity?: number,
        public batchSupplier?: BaseEntity,
    ) {
    }
}
