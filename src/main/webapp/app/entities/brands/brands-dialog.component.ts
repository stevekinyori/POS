import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Brands } from './brands.model';
import { BrandsPopupService } from './brands-popup.service';
import { BrandsService } from './brands.service';
import { Products, ProductsService } from '../products';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-brands-dialog',
    templateUrl: './brands-dialog.component.html'
})
export class BrandsDialogComponent implements OnInit {

    brands: Brands;
    isSaving: boolean;

    products: Products[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private brandsService: BrandsService,
        private productsService: ProductsService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.productsService.query()
            .subscribe((res: ResponseWrapper) => { this.products = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.brands.id !== undefined) {
            this.subscribeToSaveResponse(
                this.brandsService.update(this.brands));
        } else {
            this.subscribeToSaveResponse(
                this.brandsService.create(this.brands));
        }
    }

    private subscribeToSaveResponse(result: Observable<Brands>) {
        result.subscribe((res: Brands) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Brands) {
        this.eventManager.broadcast({ name: 'brandsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackProductsById(index: number, item: Products) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-brands-popup',
    template: ''
})
export class BrandsPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private brandsPopupService: BrandsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.brandsPopupService
                    .open(BrandsDialogComponent as Component, params['id']);
            } else {
                this.brandsPopupService
                    .open(BrandsDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
