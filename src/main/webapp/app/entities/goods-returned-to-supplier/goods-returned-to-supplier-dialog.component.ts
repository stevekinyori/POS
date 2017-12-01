import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { GoodsReturnedToSupplier } from './goods-returned-to-supplier.model';
import { GoodsReturnedToSupplierPopupService } from './goods-returned-to-supplier-popup.service';
import { GoodsReturnedToSupplierService } from './goods-returned-to-supplier.service';
import { Suppliers, SuppliersService } from '../suppliers';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-goods-returned-to-supplier-dialog',
    templateUrl: './goods-returned-to-supplier-dialog.component.html'
})
export class GoodsReturnedToSupplierDialogComponent implements OnInit {

    goodsReturnedToSupplier: GoodsReturnedToSupplier;
    isSaving: boolean;

    batchsuppliers: Suppliers[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private goodsReturnedToSupplierService: GoodsReturnedToSupplierService,
        private suppliersService: SuppliersService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.suppliersService
            .query({filter: 'goodsreturned-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.goodsReturnedToSupplier.batchSupplier || !this.goodsReturnedToSupplier.batchSupplier.id) {
                    this.batchsuppliers = res.json;
                } else {
                    this.suppliersService
                        .find(this.goodsReturnedToSupplier.batchSupplier.id)
                        .subscribe((subRes: Suppliers) => {
                            this.batchsuppliers = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.goodsReturnedToSupplier.id !== undefined) {
            this.subscribeToSaveResponse(
                this.goodsReturnedToSupplierService.update(this.goodsReturnedToSupplier));
        } else {
            this.subscribeToSaveResponse(
                this.goodsReturnedToSupplierService.create(this.goodsReturnedToSupplier));
        }
    }

    private subscribeToSaveResponse(result: Observable<GoodsReturnedToSupplier>) {
        result.subscribe((res: GoodsReturnedToSupplier) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: GoodsReturnedToSupplier) {
        this.eventManager.broadcast({ name: 'goodsReturnedToSupplierListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSuppliersById(index: number, item: Suppliers) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-goods-returned-to-supplier-popup',
    template: ''
})
export class GoodsReturnedToSupplierPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private goodsReturnedToSupplierPopupService: GoodsReturnedToSupplierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.goodsReturnedToSupplierPopupService
                    .open(GoodsReturnedToSupplierDialogComponent as Component, params['id']);
            } else {
                this.goodsReturnedToSupplierPopupService
                    .open(GoodsReturnedToSupplierDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
