import { BaseEntity } from './../../shared';

export class Brands implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public shtDesc?: string,
        public description?: string,
        public products?: BaseEntity,
    ) {
    }
}
