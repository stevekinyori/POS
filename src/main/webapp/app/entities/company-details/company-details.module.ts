import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    COMPANY_DETAILSService,
    COMPANY_DETAILSPopupService,
    COMPANY_DETAILSComponent,
    COMPANY_DETAILSDetailComponent,
    COMPANY_DETAILSDialogComponent,
    COMPANY_DETAILSPopupComponent,
    COMPANY_DETAILSDeletePopupComponent,
    COMPANY_DETAILSDeleteDialogComponent,
    cOMPANY_DETAILSRoute,
    cOMPANY_DETAILSPopupRoute,
} from './';

const ENTITY_STATES = [
    ...cOMPANY_DETAILSRoute,
    ...cOMPANY_DETAILSPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        COMPANY_DETAILSComponent,
        COMPANY_DETAILSDetailComponent,
        COMPANY_DETAILSDialogComponent,
        COMPANY_DETAILSDeleteDialogComponent,
        COMPANY_DETAILSPopupComponent,
        COMPANY_DETAILSDeletePopupComponent,
    ],
    entryComponents: [
        COMPANY_DETAILSComponent,
        COMPANY_DETAILSDialogComponent,
        COMPANY_DETAILSPopupComponent,
        COMPANY_DETAILSDeleteDialogComponent,
        COMPANY_DETAILSDeletePopupComponent,
    ],
    providers: [
        COMPANY_DETAILSService,
        COMPANY_DETAILSPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosCOMPANY_DETAILSModule {}
