import { BaseEntity } from './../../shared';

export const enum Measurement {
    'LITRES',
    'MILLILITRES'
}

export class Packaging implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public quantity?: number,
        public capacity?: number,
        public unit?: Measurement,
    ) {
    }
}
