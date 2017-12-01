import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    SalesService,
    SalesPopupService,
    SalesComponent,
    SalesDetailComponent,
    SalesDialogComponent,
    SalesPopupComponent,
    SalesDeletePopupComponent,
    SalesDeleteDialogComponent,
    salesRoute,
    salesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...salesRoute,
    ...salesPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SalesComponent,
        SalesDetailComponent,
        SalesDialogComponent,
        SalesDeleteDialogComponent,
        SalesPopupComponent,
        SalesDeletePopupComponent,
    ],
    entryComponents: [
        SalesComponent,
        SalesDialogComponent,
        SalesPopupComponent,
        SalesDeleteDialogComponent,
        SalesDeletePopupComponent,
    ],
    providers: [
        SalesService,
        SalesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosSalesModule {}
