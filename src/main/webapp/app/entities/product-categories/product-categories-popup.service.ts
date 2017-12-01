import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ProductCategories } from './product-categories.model';
import { ProductCategoriesService } from './product-categories.service';

@Injectable()
export class ProductCategoriesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private productCategoriesService: ProductCategoriesService

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
                this.productCategoriesService.find(id).subscribe((productCategories) => {
                    productCategories.dateCreated = this.datePipe
                        .transform(productCategories.dateCreated, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.productCategoriesModalRef(component, productCategories);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.productCategoriesModalRef(component, new ProductCategories());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    productCategoriesModalRef(component: Component, productCategories: ProductCategories): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.productCategories = productCategories;
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
