import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Suppliers } from './suppliers.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SuppliersService {

    private resourceUrl = SERVER_API_URL + 'api/suppliers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/suppliers';

    constructor(private http: Http) { }

    create(suppliers: Suppliers): Observable<Suppliers> {
        const copy = this.convert(suppliers);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(suppliers: Suppliers): Observable<Suppliers> {
        const copy = this.convert(suppliers);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Suppliers> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Suppliers.
     */
    private convertItemFromServer(json: any): Suppliers {
        const entity: Suppliers = Object.assign(new Suppliers(), json);
        return entity;
    }

    /**
     * Convert a Suppliers to a JSON which can be sent to the server.
     */
    private convert(suppliers: Suppliers): Suppliers {
        const copy: Suppliers = Object.assign({}, suppliers);
        return copy;
    }
}
