import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    BrandsService,
    BrandsPopupService,
    BrandsComponent,
    BrandsDetailComponent,
    BrandsDialogComponent,
    BrandsPopupComponent,
    BrandsDeletePopupComponent,
    BrandsDeleteDialogComponent,
    brandsRoute,
    brandsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...brandsRoute,
    ...brandsPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        BrandsComponent,
        BrandsDetailComponent,
        BrandsDialogComponent,
        BrandsDeleteDialogComponent,
        BrandsPopupComponent,
        BrandsDeletePopupComponent,
    ],
    entryComponents: [
        BrandsComponent,
        BrandsDialogComponent,
        BrandsPopupComponent,
        BrandsDeleteDialogComponent,
        BrandsDeletePopupComponent,
    ],
    providers: [
        BrandsService,
        BrandsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosBrandsModule {}
