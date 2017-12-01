import { BaseEntity } from './../../shared';

export class COMPANY_DETAILS implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public location?: string,
        public dateOpened?: any,
        public licenceNo?: string,
    ) {
    }
}
