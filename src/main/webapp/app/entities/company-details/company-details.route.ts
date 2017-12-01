import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { COMPANY_DETAILSComponent } from './company-details.component';
import { COMPANY_DETAILSDetailComponent } from './company-details-detail.component';
import { COMPANY_DETAILSPopupComponent } from './company-details-dialog.component';
import { COMPANY_DETAILSDeletePopupComponent } from './company-details-delete-dialog.component';

export const cOMPANY_DETAILSRoute: Routes = [
    {
        path: 'company-details',
        component: COMPANY_DETAILSComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'COMPANY_DETAILS'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'company-details/:id',
        component: COMPANY_DETAILSDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'COMPANY_DETAILS'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cOMPANY_DETAILSPopupRoute: Routes = [
    {
        path: 'company-details-new',
        component: COMPANY_DETAILSPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'COMPANY_DETAILS'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'company-details/:id/edit',
        component: COMPANY_DETAILSPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'COMPANY_DETAILS'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'company-details/:id/delete',
        component: COMPANY_DETAILSDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'COMPANY_DETAILS'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
