import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    SaleTransactionsService,
    SaleTransactionsPopupService,
    SaleTransactionsComponent,
    SaleTransactionsDetailComponent,
    SaleTransactionsDialogComponent,
    SaleTransactionsPopupComponent,
    SaleTransactionsDeletePopupComponent,
    SaleTransactionsDeleteDialogComponent,
    saleTransactionsRoute,
    saleTransactionsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...saleTransactionsRoute,
    ...saleTransactionsPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SaleTransactionsComponent,
        SaleTransactionsDetailComponent,
        SaleTransactionsDialogComponent,
        SaleTransactionsDeleteDialogComponent,
        SaleTransactionsPopupComponent,
        SaleTransactionsDeletePopupComponent,
    ],
    entryComponents: [
        SaleTransactionsComponent,
        SaleTransactionsDialogComponent,
        SaleTransactionsPopupComponent,
        SaleTransactionsDeleteDialogComponent,
        SaleTransactionsDeletePopupComponent,
    ],
    providers: [
        SaleTransactionsService,
        SaleTransactionsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosSaleTransactionsModule {}
