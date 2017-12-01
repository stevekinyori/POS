import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SuppliersComponent } from './suppliers.component';
import { SuppliersDetailComponent } from './suppliers-detail.component';
import { SuppliersPopupComponent } from './suppliers-dialog.component';
import { SuppliersDeletePopupComponent } from './suppliers-delete-dialog.component';

export const suppliersRoute: Routes = [
    {
        path: 'suppliers',
        component: SuppliersComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Suppliers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'suppliers/:id',
        component: SuppliersDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Suppliers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const suppliersPopupRoute: Routes = [
    {
        path: 'suppliers-new',
        component: SuppliersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Suppliers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'suppliers/:id/edit',
        component: SuppliersPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Suppliers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'suppliers/:id/delete',
        component: SuppliersDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Suppliers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
