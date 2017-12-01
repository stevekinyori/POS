import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SalesComponent } from './sales.component';
import { SalesDetailComponent } from './sales-detail.component';
import { SalesPopupComponent } from './sales-dialog.component';
import { SalesDeletePopupComponent } from './sales-delete-dialog.component';

export const salesRoute: Routes = [
    {
        path: 'sales',
        component: SalesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sales/:id',
        component: SalesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const salesPopupRoute: Routes = [
    {
        path: 'sales-new',
        component: SalesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sales/:id/edit',
        component: SalesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sales/:id/delete',
        component: SalesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
