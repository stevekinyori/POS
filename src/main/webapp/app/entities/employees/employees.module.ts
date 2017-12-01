import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PosSharedModule } from '../../shared';
import {
    EmployeesService,
    EmployeesPopupService,
    EmployeesComponent,
    EmployeesDetailComponent,
    EmployeesDialogComponent,
    EmployeesPopupComponent,
    EmployeesDeletePopupComponent,
    EmployeesDeleteDialogComponent,
    employeesRoute,
    employeesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...employeesRoute,
    ...employeesPopupRoute,
];

@NgModule({
    imports: [
        PosSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        EmployeesComponent,
        EmployeesDetailComponent,
        EmployeesDialogComponent,
        EmployeesDeleteDialogComponent,
        EmployeesPopupComponent,
        EmployeesDeletePopupComponent,
    ],
    entryComponents: [
        EmployeesComponent,
        EmployeesDialogComponent,
        EmployeesPopupComponent,
        EmployeesDeleteDialogComponent,
        EmployeesDeletePopupComponent,
    ],
    providers: [
        EmployeesService,
        EmployeesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PosEmployeesModule {}
