import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PackagingComponent } from './packaging.component';
import { PackagingDetailComponent } from './packaging-detail.component';
import { PackagingPopupComponent } from './packaging-dialog.component';
import { PackagingDeletePopupComponent } from './packaging-delete-dialog.component';

export const packagingRoute: Routes = [
    {
        path: 'packaging',
        component: PackagingComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packagings'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'packaging/:id',
        component: PackagingDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packagings'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const packagingPopupRoute: Routes = [
    {
        path: 'packaging-new',
        component: PackagingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packagings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'packaging/:id/edit',
        component: PackagingPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packagings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'packaging/:id/delete',
        component: PackagingDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Packagings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
