import { BaseEntity } from './../../shared';

export class Sales implements BaseEntity {
    constructor(
        public id?: number,
        public dateInitiated?: any,
        public userId?: number,
        public totalSaleAmount?: number,
        public totalPaidAmount?: number,
        public dateClosed?: any,
        public transactions?: BaseEntity[],
    ) {
    }
}
