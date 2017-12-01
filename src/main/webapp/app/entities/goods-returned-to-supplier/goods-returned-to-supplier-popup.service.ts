import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { GoodsReturnedToSupplier } from './goods-returned-to-supplier.model';
import { GoodsReturnedToSupplierService } from './goods-returned-to-supplier.service';

@Injectable()
export class GoodsReturnedToSupplierPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private goodsReturnedToSupplierService: GoodsReturnedToSupplierService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.goodsReturnedToSupplierService.find(id).subscribe((goodsReturnedToSupplier) => {
                    this.ngbModalRef = this.goodsReturnedToSupplierModalRef(component, goodsReturnedToSupplier);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.goodsReturnedToSupplierModalRef(component, new GoodsReturnedToSupplier());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    goodsReturnedToSupplierModalRef(component: Component, goodsReturnedToSupplier: GoodsReturnedToSupplier): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.goodsReturnedToSupplier = goodsReturnedToSupplier;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
